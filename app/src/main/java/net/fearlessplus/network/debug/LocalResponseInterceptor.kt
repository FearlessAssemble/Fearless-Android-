package net.fearlessplus.network.debug

import android.content.Context
import io.sbox.library.extensions.trace
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class LocalResponseInterceptor(ctx: Context) : Interceptor {
    private val context: Context
    private var scenario: String? = null
    fun setScenario(scenario: String?) {
        this.scenario = scenario
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestedUrl: URL = request.url.toUrl()
        val requestedMethod: String = request.method

        if(requestedUrl.host == "localhost") {

            var prefix = ""
            if (scenario != null) {
                prefix = scenario + "_"
            }
            var fileName =
                (prefix + requestedMethod + requestedUrl.getPath()).replace("/", "_")
            fileName = fileName.toLowerCase()

            if(fileName.startsWith("get_gallery")) {
                fileName = "get_gallery"
            }

            val resourceId: Int = context.getResources().getIdentifier(
                fileName, "raw",
                context.getPackageName()
            )

            var filePath = "res/raw/$fileName.json"
            if (resourceId == 0) {
//            Log.wtf("YourTag", "Could not find res/raw/$fileName.json")
                trace("##LocalResponseInterceptor## Error res/raw/$fileName.json")
                throw IOException("Could not find res/raw/$fileName.json")
            }
            val inputStream: InputStream = context.getResources().openRawResource(resourceId)

            var mimeType: String? = URLConnection.guessContentTypeFromStream(inputStream)
            if (mimeType == null) {
                mimeType = "application/json"
            }
            var input: Buffer = Buffer().readFrom(inputStream)

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("")
                .body(ResponseBody.create(
                    mimeType.toMediaTypeOrNull(),
                    input.size,
                    input
                ))
                .build()
        }
        return chain.proceed(request)
    }

    /*
    File file = new File(path + fileName);
    file.toURL().openConnection().getContentType(); // 1
    URLConnection.guessContentTypeFromName(orgFile.getName()); // 2
    URLConnection.guessContentTypeFromStream(new BufferedInputStream(new FileInputStream(orgFile))); // 3
    new MimetypesFileTypeMap().getContentType(orgFile); // 4
    Files.probeContentType(Paths.get(orgFile.toURI())); // 5

     */

    init {
        context = ctx
    }
}
package org.anitab.mentorship.remote

import org.anitab.mentorship.BuildConfig

/**
 * Object to keep settings such as URL parts, regions, s3 bucket names.
 */
object BaseUrl {

    private const val PROTOCOL_HTTPS = "http://"
    private const val PRODUCTION_URL = "anitab-mentorship"
    private const val DEVELOPMENT_URL = "anitab-mentorship-dev"
    private const val EB_REGION = ".eu-central-1.elasticbeanstalk.com/"

    private const val PROTOCOL_S3 = "s3://"
    private const val S3_BUCKET_NAME = "anitab-mentorship/"
    private const val S3_PRODUCTION_DIRECTORY = "prod/"
    private const val S3_DEVELOPMENT_DIRECTORY = "dev/"
    private const val LOCALHOST_URL = "10.0.2.2"
    private const val LOCALHOST_PORT = ":5000"

    val apiBaseUrl: String
        get() = if (BuildConfig.BUILD_TYPE == "debug") {
            // "$PROTOCOL_HTTPS$DEVELOPMENT_URL$EB_REGION"
            // TODO: once backend is fixed remove this temporary URL
            "https://mentorship-backend-temp.herokuapp.com/"
        } else if (BuildConfig.BUILD_TYPE == "debug_localhost") {
            "$PROTOCOL_HTTPS$LOCALHOST_URL$LOCALHOST_PORT"
        } else {
            "$PROTOCOL_HTTPS$PRODUCTION_URL$EB_REGION"
        }

    val s3BaseUrl: String
        get() = if (BuildConfig.DEBUG) {
            "$PROTOCOL_S3$S3_BUCKET_NAME$S3_DEVELOPMENT_DIRECTORY"
        } else {
            "$PROTOCOL_S3$S3_BUCKET_NAME$S3_PRODUCTION_DIRECTORY"
        }
}

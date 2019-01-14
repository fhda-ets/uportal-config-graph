package edu.fhda.uportal.confgraph


import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

import java.nio.file.Paths
/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
class ImportFileTask extends AbstractImportTask {

    // Parameters that are required
    String pathSourceFile

    @Option(option = 'file', description = 'Set the filename of the graph to be imported')
    void setFile(final String path) {
        this.pathSourceFile = path
    }

    @Override
    @TaskAction
    def action() {
        // Call parent method
        super.action()

        // Validate parameters
        if(pathSourceFile == null) {
            throw new RuntimeException("Parameter --file is required to set the graph file to be imported")
        }

        // Create reference to input file
        File sourceFile = Paths.get(pathSourceFile).toFile()

        // Create HTTP request body
        RequestBody body = createHttpBody(sourceFile)

        // Create HTTP request
        Request request = new Request.Builder()
            .url(configGraphUrl + "/admin/import")
            .header("Authorization", "Bearer ${jwtSignedKey}")
            .post(body)
            .build()

        // Execute
        logger.quiet("Importing ${sourceFile.name}")
        Response response = httpClient.newCall(request).execute()
        logger.info(response.body().string())
    }
    
}
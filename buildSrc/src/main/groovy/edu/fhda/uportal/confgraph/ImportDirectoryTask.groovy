package edu.fhda.uportal.confgraph

import groovy.io.FileType
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
class ImportDirectoryTask extends AbstractImportTask {

    // Parameters that are required
    String pathSourceDirectory

    @Option(option = 'directory', description = 'Set the directory of graph files to be imported')
    void setDirectory(final String path) {
        this.pathSourceDirectory = path
    }

    @Override
    @TaskAction
    def action() {
        // Call parent method
        super.action()

        // Validate parameters
        if(pathSourceDirectory == null) {
            throw new RuntimeException("Parameter --directory is required to set the directory of graph files to import")
        }

        // Create reference to input directory
        File sourceDirectory = Paths.get(pathSourceDirectory).toFile()
        
        // Recursively iterate through directory
        sourceDirectory.eachFileRecurse(FileType.FILES, { sourceFile ->
            // Filter to only supported files
            if(!(isSupportedFile(sourceFile))) {
                // Skip
                return
            }

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
        })
    }
    
}
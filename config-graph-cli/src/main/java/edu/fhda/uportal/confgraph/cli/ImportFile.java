package edu.fhda.uportal.confgraph.cli;

import okhttp3.Request;
import okhttp3.RequestBody;
import picocli.CommandLine;

import java.nio.file.Path;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@CommandLine.Command(name="import-file")
public class ImportFile implements Runnable {

    @CommandLine.ParentCommand
    private CliRunner.RootCommand root;

    @CommandLine.Option(names = "--file", description = "Graph file to import")
    private Path inputFile;

    @Override
    public void run() {
        System.out.println("Running import-file task");
        System.out.format("Importing file %s ... ", inputFile.toString());

        try {
            // Create HTTP request body
            RequestBody body = PathUtils.createHttpBody(inputFile);

            // Create HTTP request
            Request request = new Request.Builder()
                .url(root.configGraphUrl + "/admin/import")
                .header("Authorization", "Bearer " + root.jwtSignedKey)
                .post(body)
                .build();

            // Execute API call
            root.httpClient.newCall(request).execute();

            System.out.println("success");
        }
        catch(Exception error) {
            System.out.println("failed");
            throw new RuntimeException(error);
        }
    }

}

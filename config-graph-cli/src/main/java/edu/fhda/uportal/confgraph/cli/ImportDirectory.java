package edu.fhda.uportal.confgraph.cli;

import okhttp3.Request;
import okhttp3.RequestBody;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author mrapczynski, Foothill-De Anza College District, rapczynskimatthew@fhda.edu
 * @version 1.0
 */
@CommandLine.Command(name="import-directory", aliases = {"import-dir"})
@SuppressWarnings("Duplicates")
public class ImportDirectory implements Runnable {

    @CommandLine.ParentCommand
    private CliRunner.RootCommand root;

    @CommandLine.Parameters
    private Path inputDirectory;

    @Override
    public void run() {
        System.out.format("Walking directory %s%n", inputDirectory.toAbsolutePath().toString());

        try {
            // Walk directory
            Files.walk(inputDirectory)
                .filter(Files::isRegularFile)
                .filter(PathUtils::isSupportedFile)
                .forEach(this::importFile);
        }
        catch(Exception error) {
            throw new RuntimeException(error);
        }
    }

    void importFile(Path inputFile) {
        System.out.println("Running import-directory task");
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

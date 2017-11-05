package md.utm.pad.labs.client;

import md.utm.pad.labs.channel.ClientChannel;
import md.utm.pad.labs.channel.SocketClientChannel;
import md.utm.pad.labs.config.ClientConfiguration;
import md.utm.pad.labs.interogator.NodeInterogator;
import md.utm.pad.labs.request.Request;
import md.utm.pad.labs.response.DiscoverResponse;
import md.utm.pad.labs.response.Response;
import md.utm.pad.labs.service.impl.JacksonJsonService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

/**
 * Created by anrosca on Nov, 2017
 */
public class Client implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(Client.class);

    private final NodeInterogator interogator;
    private final JacksonJsonService jsonService;
    private MavenNode mavenNode;
    private ClientChannel channel;

    public Client(ClientConfiguration configuration, JacksonJsonService jsonService) {
        interogator = new NodeInterogator(configuration, jsonService);
        this.jsonService = jsonService;
    }

    public void detectMavenNode() {
        interogator.interogateNodes();
        Optional<DiscoverResponse> maven = interogator.getNodes().stream()
                .sorted()
                .findFirst();
        maven.ifPresent(r -> {
            mavenNode =  new MavenNode(r.getNodeAddress(), r.getNodePort());
        });
        LOGGER.info("Maven node: " + mavenNode);
    }

    public void connectToMaven() {
        try {
            Socket socket = new Socket(mavenNode.getAddress(), mavenNode.getPort());
            channel = new SocketClientChannel(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        channel.close();
    }

    public void getAll() {
        Request request = new Request("from Student");
        channel.write(jsonService.toJson(request));
        Response response = jsonService.fromJson(readJsonRequest(), Response.class);
        System.out.println(response);
    }

    private String readJsonRequest() {
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = channel.readLine()) != null && line.trim().length() > 0) {
            requestBuilder.append(line);
        }
        return requestBuilder.toString();
    }
}

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer

import java.util.concurrent.Executors

// a fake npm registry that accepts every publish request and records its authorization header
File requests = new File(basedir, "registry-requests.log")
requests.text = ""

HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 48732), 0)

server.createContext("/") { HttpExchange exchange ->

    exchange.requestBody.bytes

    synchronized (requests) {
        requests << "${exchange.requestMethod} ${exchange.requestURI.rawPath} ${exchange.requestHeaders.getFirst("Authorization")}\n"
    }

    boolean publish = exchange.requestMethod == "PUT"
    byte[] body = (publish ? '{"ok":true}' : '{"error":"not found"}').bytes

    exchange.responseHeaders.add("Content-Type", "application/json")
    exchange.sendResponseHeaders(publish ? 200 : 404, body.length)
    exchange.responseBody.withCloseable { it.write(body) }
}

server.executor = Executors.newCachedThreadPool { Runnable runnable ->
    Thread thread = new Thread(runnable)
    thread.daemon = true
    return thread
}
server.start()

context.put("registry", server)

return true

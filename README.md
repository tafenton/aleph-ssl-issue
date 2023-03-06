Procedure for reproducing the issue:

1. Start figwheel-main using "lein fig:dev"
2. Start REPL
3. Run (minimal-example.clj.core/start-dev); this starts server *without* SSL
4. Make changes to web.cljs, save, and observe changes compile and propagate to page served via aleph
5. Run (minimal-example.clj.core/start), which stops the running server and starts a new one *with* SSL
6. Make changes to web.cljs, save, and observe that compilation fails due to:


    java.io.FileNotFoundException: (The requested operation cannot be performed on a file with a user-mapped section open)

Tested in Azul Zulu 1.8, Azul Zulu 19.0.2, and Oracle Java 1.8.0_361.

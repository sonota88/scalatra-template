# My Scalatra Web App #

- ポートの変更 => `build.sbt`

## Build & Run ##

```sh
$ cd my-scalatra-web-app
$ sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

---

auto reload

```
$ sbt
~;jetty:stop;jetty:start
```

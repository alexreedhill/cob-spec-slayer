(ns cob-spec-slayer.core
  (:require [lazy-server.server :as server]
            [lazy-server.router :refer :all]
            [lazy-server.response-builder :refer [redirect serve-file]]
            [lazy-server.basic-authenticator :refer [basic-auth]]
            [clojure.string :refer [join]]
            [clojure.java.io :refer [as-relative-path]])
  (:gen-class :main true))

(defn param-decode-response [request]
  (str
    (name (first (second (request :query-params)))) " = "
    (second (second (request :query-params))) "\n"
    (name (first (first (request :query-params)))) " = "
    (second (first (request :query-params)))))

(defrouter cob-spec-router request
  (GET "/" {:body "" :code 200})
  (GET "/parameters" {:body (cob-spec-slayer.core/param-decode-response request) :code 200})
  (GET "/redirect" (redirect "http://localhost:5000/"))
  (GET "/file1" (serve-file request))
  (GET "/text-file.txt" (serve-file request))
  (GET "/image.jpeg" (serve-file request))
  (GET "/image.png" (serve-file request))
  (GET "/image.gif" (serve-file request))
  (GET "/partial_content.txt" (serve-file request))
  (GET "/logs" (basic-auth request "Authentication required" (slurp "public/log.txt") "admin:hunter2"))
  (POST "/form" {:code 200})
  (PUT "/form" {:code 200})
  (OPTIONS "/method_options" {:code 200})
  (not-found "Sorry, there's nothing here!"))

(defn -main [& args]
  (server/-main "5000" "localhost" cob-spec-router))

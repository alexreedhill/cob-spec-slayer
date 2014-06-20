(ns cob-spec-slayer.core
  (:require [lazy-server.server :as server]
            [lazy-server.router :refer :all]
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
  (GET "/logs" (basic-auth request "Authentication required" (slurp "public/log.txt") "admin:hunter2"))
  (POST "/form" (save-resource request))
  (PUT "/form" (save-resource request))
  (PATCH "/patch-content.txt" (save-resource request))
  (OPTIONS "/method_options" {:code 200})
  (not-found "Sorry, there's nothing here!"))

(defn -main [& args]
  (server/-main "5000" "localhost" cob-spec-router))

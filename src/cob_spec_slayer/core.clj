(ns cob-spec-slayer.core
  (:require [lazy-server.server :as server]
            [lazy-server.router :refer :all]
            [lazy-server.basic-authenticator :refer [authenticate]]
            [lazy-server.file-interactor :refer [directory-contents]]
            [clojure.string :refer [trim-newline]])
  (:gen-class :main true))

(defn param-decode-body [request]
  (->> (request :query-params)
       (sort)
       (map #(str (key %) " = " (val %) "\n"))
       (apply str)
       (trim-newline)))

(defn param-decode-response [request]
  {:code 200 :body (param-decode-body request)})

(defn directory-body []
  (->> (directory-contents public-dir)
       (map #(str "<a href=\"/" % "\">" % "</a><br>\n"))
       (mapcat #(.getBytes %))
       (byte-array)))

(defn directory-response
  {:code 200 :body (directory-body)})

(defrouter cob-spec-router request
  (GET "/" (cob-spec-slayer.core/directory-response))
  (GET "/parameters" (cob-spec-slayer.core/param-decode-response request) :code 200)
  (GET "/redirect" (redirect "http://localhost:5000/"))
  (GET "/logs" (authenticate request "Authentication required" (slurp "public/log.txt") "admin:hunter2"))
  (POST "/form" (save-resource request))
  (PUT "/form" (save-resource request))
  (PATCH "/patch-content.txt" (save-resource request))
  (OPTIONS "/method_options" {:code 200})
  (not-found "Sorry, there's nothing here!"))

(defn -main [& args]
  (server/-main "5000" "localhost" cob-spec-router "public/"))

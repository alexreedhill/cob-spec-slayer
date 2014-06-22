(ns cob-spec-slayer.core
  (:require [lazy-server.server :as server]
            [lazy-server.router :refer :all]
            [lazy-server.basic-authenticator :refer [basic-auth]]
            [lazy-server.file-interactor :refer [directory-contents]]
            [clojure.string :refer [trim-newline]])
  (:gen-class :main true))

(defn param-decode-response [request]
  (->> (request :query-params)
       (sort)
       (map #(str (key %) " = " (val %) "\n"))
       (apply str)
       (trim-newline)))

(defn directory-response []
  (->> (directory-contents public-dir)
       (map #(str "<a href=\"/" % "\">" % "</a><br>\n"))
       (mapcat #(.getBytes %))
       (byte-array)))

(defrouter cob-spec-router request
  (GET "/" {:code 200 :body (cob-spec-slayer.core/directory-response)})
  (GET "/parameters" {:body (cob-spec-slayer.core/param-decode-response request) :code 200})
  (GET "/redirect" (redirect "http://localhost:5000/"))
  (GET "/logs" (basic-auth request "Authentication required" (slurp "public/log.txt") "admin:hunter2"))
  (POST "/form" (save-resource request))
  (PUT "/form" (save-resource request))
  (PATCH "/patch-content.txt" (save-resource request))
  (OPTIONS "/method_options" {:code 200})
  (not-found "Sorry, there's nothing here!"))

(defn -main [& args]
  (server/-main "5000" "localhost" cob-spec-router "public/"))

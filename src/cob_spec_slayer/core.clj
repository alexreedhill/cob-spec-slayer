(ns cob-spec-slayer.core
  (:require [lazy-server.server :as server]
            [lazy-server.router :refer [defrouter GET four-oh-four]]
            [clojure.string :refer [join]])
  (:gen-class :main true))

(defn param-decode-response [request]
  (join \newline
        [(str
           (name (first (second (request :query-params)))) " = "
           (second (second (request :query-params))))
         (str
           (name (first (first (request :query-params)))) " = "
           (second (first (request :query-params))))]))

(defrouter cob-spec-router request
  (GET "/" {:body "" :code 200})
  (GET "/parameters" {:body (param-decode-response request) :code 200})
  (four-oh-four "Sorry, there's nothing here!"))

(defn -main [& args]
  (server/-main "5000" "localhost" cob-spec-router))

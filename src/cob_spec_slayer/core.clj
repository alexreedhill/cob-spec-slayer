(ns cob-spec-slayer.core
  (:require [lazy-server.server :as server]
            [lazy-server.router :refer [defrouter]])
  (:gen-class :main true))

(defrouter cob-spec-router
  (GET "/" {:body "" :code 200}))

(defn -main [& args]
  (server/-main "5000" "localhost" cob-spec-router))

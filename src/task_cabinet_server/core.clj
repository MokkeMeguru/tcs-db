(ns task-cabinet-server.core
  (:gen-class)
  (:require [environ.core :refer [env]]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [integrant.repl :as igr]))


(def config-file
  "config.edn")

(defn load-config [config]
  (-> config
      io/resource
      slurp
      ig/read-string
      (doto
          ig/load-namespaces)))

(defn start []
  (igr/set-prep! (constantly (load-config config-file)))
  (igr/prep)
  (igr/init))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

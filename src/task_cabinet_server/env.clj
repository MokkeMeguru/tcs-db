(ns task-cabinet-server.env
  (:require [environ.core :refer [env]]
            [taoensso.timbre :as timbre]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [integrant.repl :as igr]))


(defmethod ig/init-key ::env [_ _]
  {:dsatabase-url (env :database-url)})

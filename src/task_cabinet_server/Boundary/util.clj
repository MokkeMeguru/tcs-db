(ns task-cabinet-server.Boundary.util
  (:require [integrant.core :as ig]))

(defrecord Boundary [spec])

(defmethod ig/init-key ::sql [_ spec]
  (->Boundary spec))

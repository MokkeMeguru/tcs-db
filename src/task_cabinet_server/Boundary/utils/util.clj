(ns task-cabinet-server.Boundary.utils.util
  (:require
   [next.jdbc :as jdbc]
   [clj-time.core :as t]
   [clj-time.coerce :as tc]
   [clojure.string :as string]
   [buddy.hashers :as hashers]
   [camel-snake-kebab.core :refer [->kebab-case ->snake_case]]
   [camel-snake-kebab.extras :refer [transform-keys]]))

(defn match-regex [regex s]
  (re-matches regex s))

(defn check-whitespace [s]
  (= (count s) (count (string/trim s))))

(defn hash-password [password]
  (hashers/derive password))

 (defn check-identity [password pass-list]
  (filter #(hashers/check password (:users/password %)) pass-list))

(defn long-to-sql [long-time]
  (-> long-time
      tc/from-long
      tc/to-sql-time time))

(defn sql-now []
  (tc/to-sql-time (t/now)))

(defn transform-keys-to-snake [m]
  (transform-keys #(->snake_case % :separator \-) m))

(defn transform-keys-to-kebab [m]
  (transform-keys #(->kebab-case % :separator \_) m))

(defn run-sql [spec sql-command-list one?]
  (with-open [conn (jdbc/get-connection (:datasource spec))]
    (if one?
      (jdbc/execute-one! conn sql-command-list)
      (jdbc/execute! conn sql-command-list))))

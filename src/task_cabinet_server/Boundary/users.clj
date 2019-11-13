(ns task-cabinet-server.Boundary.users
  (:require [next.jdbc :as jdbc]
            [honeysql.core :as sql]
            [next.jdbc.sql :as njs]
            [honeysql.helpers :as h]
            [task-cabinet-server.Boundary.utils.util :as utils]
            [task-cabinet-server.Boundary.utils.sql :as s]))

(defprotocol Users
  (get-users [db])
  (get-user [db user])
  (create-user [db user])
  (update-password [db user])
  (delete-user [db user])
  (erase-user[db  user]))

(extend-protocol Users
  task_cabinet_server.Boundary.utils.sql.Boundary
  (get-users [{:keys [spec]}]
    (let [scl (sql/format
               {:select [:users/id :users/name]
                :from [:users]})]
      (utils/run-sql spec scl false)))
  (get-user [{:keys [spec]} {:keys [users/name users/password]}]
    (let [scl (sql/format
               {:select [:*]
                :from [:users]
                :where  [:= :users/name name]})
          candidate (utils/run-sql spec scl false)]
      (utils/check-identity password candidate)))
  (create-user [{:keys [spec]} user]
    (let [scl (sql/format
               (-> (h/insert-into :users)
                   (h/values [user])))]
      (utils/run-sql spec scl true)))
  (update-password [{:keys [spec]} {:keys [users/id users/password]}]
    (let [scl (sql/format
               (-> (h/update :users)
                   (h/sset
                    {:users/password password
                     :users/updated_at (utils/sql-now)})
                   (h/where [:= :users/id id])))]
      (utils/run-sql spec scl true)))
  (delete-user [{:keys [spec]} {:keys [users/id]}]
    (let [scl (sql/format
               (-> (h/update :users)
                   (h/sset
                    {:users/is_deleted true
                     :users/updated_at (utils/sql-now)})
                   (h/where [:= :users/id id])))]
      (utils/run-sql spec scl true)))
  (erase-user [{:keys [spec]} {:keys [users/id]}]
    (let [scl (sql/format
               (-> (h/delete-from :users)
                   (h/where [:= :users/id id])))]
      (utils/run-sql spec scl true))))


;; for debug
(defonce inst (task-cabinet-server.Boundary.utils.sql/->Boundary {:datasource (hikari-cp.core/make-datasource {:jdbc-url (environ.core/env :database-url)})}))

;; (:users/id
;;  (first
;;    (get-user inst {:users/name "meguru"
;;                    :users/password "emacs"})))

;; (get-user inst {:users/name "meguru"
;;                 :users/password "emacss"})

(defonce test-creation
  (create-user inst {:users/name "meguru"
                     :users/password (utils/hash-password "emacs")}))


;; (:next.jdbc/update-count
;;  (update-password inst
;;                   {:users/id 1 :users/password (utils/hash-password "emacs")}))


;; (get-users inst)

;; (delete-user? inst {:users/id 1})

(get-user inst {:users/name "meguru" :users/password "emacs"})

;; (erase-user inst {:users/id 4})

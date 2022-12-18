(ns myapp.db)
(require '[clojure.java.jdbc :as sql])
;(require '[monger.core :as mg])
;(require '[monger.collection :as mc])
;(require '[monger.operators :refer :all])
;(:import '[com.mongodb MongoOptions ServerAddress])
;(:import org.bson.types.ObjectId)


;good practice is to get database connection from deps.env

;(def db-connection-url (or (System/getenv "MYAPP_MONGO_URI")
;                           "mongodb://127.0.0.1/myApp-test"))
;
;(def db (-> db-connection-url
;            mg/connect-via-uri
;            :db))

(def blogs-coll "blogs")

(def sql-db {
             :classname "com.mysql.jdbc.Driver"
             :subprotocol "mysql"
             :subname "//localhost:3306/myapp-test"
             :user "root"
             :password ""
             })

(sql/query sql-db ["SELECT * FROM blogs"])

(sql/with-connection sql-db
                     (sql/insert-records blogs-coll { :title "My second blog" :body "Hello second" :created (new java.util.Date)}))

(sql/with-connection sql-db
                     (sql/insert-records blogs-coll { :title "My third blog" :body "Hello third"}))

(sql/with-connection sql-db
                     (sql/delete-rows blogs-coll ["id= ?" 2]))


(defn create-blog [title body]
      (sql/with-connection sql-db
                           (sql/insert-records blogs-coll { :title title :body body})) )

(create-blog "My 4th blog" "Hello 4th")

(defn list-blogs [] (sql/query sql-db ["SELECT * FROM blogs"]))

(list-blogs)

(for [b (list-blogs)]
     (println (:title b)))


(defn list-blogs []
      (sql/with-connection sql-db
                           (sql/with-query-results rs ["SELECT * FROM blogs"]
                                                   (apply hash-map (doall rs)))))
(list-blogs)

;(mc/insert db blogs-coll {:title     "My First Article"
;                          :body      "Helo CLJ Blog"
;                          :createdAt (new java.util.Date)})
;
;(mc/find-maps db blogs-coll)


;(defn create-blogs [title body]
;      (mc/insert db blogs-coll
;                 {:title     title
;                  :body      body
;                  :createdAt (new java.util.Date)}))

;(create-blogs "My third blog" "Hello from me three!")

;(defn list-blogs []
;      (mc/find-maps db blogs-coll))
;(list-blogs)

(defn get-blog-by-name [blog-name]
      (mc/find-maps db blogs-coll {:title blog-name}))



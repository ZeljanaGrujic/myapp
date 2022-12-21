(ns new-application.db)
(require '[clojure.java.jdbc :as sql])

(def blogs-coll "blogs")


(def sql-db {
             :classname "com.mysql.jdbc.Driver"
             :subprotocol "mysql"
             :subname "//localhost:3306/myapp-test"
             :user "root"
             :password ""
             })

(sql/query sql-db ["SELECT * FROM blogs"])
(sql/insert! sql-db blogs-coll { :title "My second blog" :body "Hello second" :created (new java.util.Date)})
(sql/insert! sql-db blogs-coll { :title "My third blog" :body "Hello third"})
(sql/delete! sql-db blogs-coll ["id= ?" 3])


(defn create-blog [title body]
  (sql/insert! sql-db blogs-coll { :title title :body body}))

(create-blog "My 4th blog" "Hello 4th")

(defn list-blogs [] (sql/query sql-db ["SELECT * FROM blogs"]))

(list-blogs)

(for [b (list-blogs)]
  (println (:title b)))

(defn get-blog-by-id [blog-id]
  (sql/query sql-db ["SELECT title, body FROM blogs WHERE id= ?" blog-id]))
(get-blog-by-id 4)

(def users-coll "users")

(defn create-user [username password]
  (sql/insert! sql-db users-coll { :username (str username) :password (str password)}))
(create-user "user1" "usr1")
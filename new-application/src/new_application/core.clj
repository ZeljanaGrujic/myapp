(ns new-application.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [hiccup.core :refer [html]]
            [hiccup.form :as form]
            [ring.util.response :as response]))
(require '[new-application.pages :as pg])
(require '[new-application.db :as db])


(def all-courses [
                  {:id 0 :name "Ekonomija" :grade "I godina" :smer "ISIT/MNG"}
                  {:id 1 :name "Numericka analiza" :grade "II godina" :smer "ISIT"}
                  {:id 2 :name "Menadzment tehnologije i razvoja" :grade "III godina" :smer "ISIT/MNG"}
                  {:id 3 :name "Modelovanje poslovnih procesa" :grade "IV godina" :smer "ISIT"}])

(defn base-page [& body]
  ;basic template for all our pages
  (html [:head [:title "BAZA ZNANJA"]]
        [:body [:h1 "Privatna radionica za clanove Baze znanja,
         mogla bih i da azuriram uvek koliko ima clanova na kom kursu"]
         [:a {:href "/baza-znanja"} [:h2 "BAZA ZNANJA"]]
         [:a {:href "/courses"} [:h3 "Upoznajte se sa kursevima"]]
         [:a {:href "/user/new"} [:h3 "Registruj se"]]
         body]))
(base-page)

(defn courses-view [all-courses]
  (html [:ul
         (map #(vector :li [:a {:href (format "/cours/%d" (:id %))} (:name %)]) all-courses)]))

(defn course-view [course]
  (html [:ul
         (map (fn [[k v]] [:li (format "%s : %s" (name k) (str v))]) course)]))
(course-view (all-courses 0))

(defn all-users {})

(defn create-user [usr pass] (assoc all-users :usr usr :pass pass ))

(create-user "usr1" "pass1")
(create-user "usr2" "pass2")
(println all-users)


(defroutes handler
           (GET "/" [] (base-page))
           (GET "/baza-znanja" [] (html [:p "Napisati neki malo uvod i istoriju baze znanja"]))
           (GET "/courses" [] (courses-view all-courses))
           (GET "/cours/:id" [id] (course-view (all-courses (read-string id))))
           (GET "/user/new" [] (pg/edit-user nil))
           (POST "/user" [username password]
             (do (db/create-user (str username) (str password))
                 (response/redirect "/"))))

(jetty/run-jetty (fn [req] (handler req)) {:port 3018 :join? false})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

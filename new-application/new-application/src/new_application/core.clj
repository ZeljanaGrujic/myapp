(ns new-application.core
  (:gen-class)
  (:require
    [ring.adapter.jetty :as jetty]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [hiccup.core :as h]
    [hiccup.form :as form]
    [ring.util.response :as resp]
    [ring.util.request :as req]
    [hiccup.page :refer [html5]]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [new-application.pages :as p]
    [new-application.db :as db]
    [ring.middleware.session :as session]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.reload :refer [wrap-reload]]))


(defn base-page [& body]
  ;basic template for all our pages
  (html5 [:head [:title "GRUJIC- agro"]]
        [:body [:h1 "Dnevnik klijenata i prodaje jaja"]
         [:a {:href "/grujicagro-info"} [:h2 "GRUJIC-agro"]]
         [:a {:href "/all-orders"} [:h3 "Pogledajte sve porudzbine"]]
         [:a {:href "/orders/new"} [:h3 "Nova porudzbina"]]
         body]))
(base-page)


(defroutes handler
           (GET "/" [] (base-page))
           (GET "/grujicagro-info" [] (html5 [:p "Napisati neki malo uvod i istoriju firme, ovde bi trebalo dodati malo neke slike"]))
           (GET "/all-orders" [] (p/orders-view (db/list-orders)))

           (GET "/order/:id" [id] (p/order-view (db/get-order-by-id (read-string id))))



           (GET "/orders/new" [] (p/form-new-order))
           (POST "/orders/new/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
                                             (db/new-order order))
                                           (resp/redirect "/")))

           (GET "/order-edit/:id" [id]
             (str (let [order (db/get-order-by-id (read-string id))] (p/edit-order order))))



           (POST "/order-edit/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
                                              (db/edit-order order))
                                            (resp/redirect "/")))


           (GET "/order-delete/:id" [id]
             (str (let [order (db/get-order-by-id (read-string id))] (p/form-delete-order order))))



           (POST "/order-delete/:id" req (do (let [order (full_name-amount-date-city_part-street-delivered-id (slurp (:body req)))]
                                             (db/delete-order order))
                                           (resp/redirect "/")))

           )



(jetty/run-jetty (fn [req] (handler req)) {:port 3018 :join? false})

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))



;;Resenje jer baca gresku kad cita polja
(defn full-name [full_name]
  ;name in format Nevena+Arsic
  (clojure.string/replace full_name #"\+" " "))
(defn street [street]
  ;name in format Nevena+Arsic
  (clojure.string/replace street #"\+" " "))
(defn full_name-amount-date-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:full_name  (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))
             :amount (clojure.string/replace (get (clojure.string/split string #"&") 1) "amount=" "")
             :do_date (clojure.string/replace (get (clojure.string/split string #"&") 2) "do_date=" "")
             :id (clojure.string/replace (get (clojure.string/split string #"&") 3) "id=" "")}] map))

(defn full_name-amount-date-location-delivered-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:full_name  (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))
             :amount (clojure.string/replace (get (clojure.string/split string #"&") 1) "amount=" "")
             :do_date (clojure.string/replace (get (clojure.string/split string #"&") 2) "do_date=" "")
             :location (clojure.string/replace (get (clojure.string/split string #"&") 3) "location=" "")
             :delivered (clojure.string/replace (get (clojure.string/split string #"&") 4) "delivered=" "")
             :id (clojure.string/replace (get (clojure.string/split string #"&") 5) "id=" "")}] map))

(defn full_name-amount-date-city_part-street-delivered-id [string]
  ;string contains name and phone in this format
  ;name=Nevena+Arsic&phone=0000&__anti-forgery-token=Unbound%3A+%23%27ring.middleware.anti-forgery%2F*anti-forgery-token*
  (let [map {:full_name  (full-name (clojure.string/replace (get (clojure.string/split string #"&") 0) "full_name=" ""))
             :amount (clojure.string/replace (get (clojure.string/split string #"&") 1) "amount=" "")
             :do_date (clojure.string/replace (get (clojure.string/split string #"&") 2) "do_date=" "")
             :city_part (clojure.string/replace (get (clojure.string/split string #"&") 3) "city_part=" "")
             :street (street (clojure.string/replace (get (clojure.string/split string #"&") 4) "street=" ""))
             :delivered (clojure.string/replace (get (clojure.string/split string #"&") 5) "delivered=" "")
             :id (clojure.string/replace (get (clojure.string/split string #"&") 6) "id=" "")}] map))
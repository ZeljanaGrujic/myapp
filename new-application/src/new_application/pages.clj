(ns new-application.pages
  (:require
    [hiccup.page :refer [html5]]
    [hiccup.form :as form]
    [ring.util.anti-forgery :refer (anti-forgery-field)]
    [new-application.db :as db]))
(require '[hiccup.core :refer [html]])


(defn base [& body]
  (html5
    [:head [:title "GRUJIC- agro"]]
    [:body
     [:h1 "Dnevnik klijenata i prodaje jaja"]
     [:a {:href "/orders/new"} "Nova porudzbina"]
     [:hr]
     body]))

;(defn base-page [& body]
;  ;basic template for all our pages
;  (html [:head [:title "New user"]]
;         [:body [:a {:href "/"}[:h1 "Baza znanja"]]
;          body]))

(defn index [body]
  (base body))

(defn order-view [{id :id full_name :full_name amount :amount do_date :do_date}]
  (html5
    [:li (format "Full_name: %s          Amount: %s          Do_date: %s" full_name amount do_date)]))

(defn orders-view [orders]
  (html5 [:ul
          (map  order-view orders)]))

;;preko atoma
(order-view (db/orders-data 1))
(orders-view db/orders-data)

(order-view (db/get-order-by-id 2))
(orders-view (db/list-orders))


(defn one-order-view [order]
  (html5 [:ul
          (map (fn [[k v]] [:li (format "%s : %s" (name k) (str v))]) order)]))
(one-order-view (db/get-order-by-id 2))


(defn edit-order [order]
  (html5
    [:body
     [:p (:id order)]
     (form/form-to [:post (if order
                            (str "/order-edit/" (:id order))
                            "/all-orders")]

                   (form/label "full_name" "Full name: ")
                   (form/text-field "full_name" (:full_name order))
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" (:amount order))
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" (:do_date order))
                   (form/hidden-field "id" (:id order))
                   (anti-forgery-field)

                   (form/submit-button "Save changes"))]))

(edit-order (db/get-order-by-id 2))

(defn form-new-order []
  (html5
    [:body
     (form/form-to [:post (str "/orders/new/" (db/get-next-id))]

                   (form/label "full_name" "Full name: ")
                   (form/text-field "full_name" "ime")
                   (form/label "amount" "Amount: ")
                   (form/text-field "amount" "amount")
                   (form/label "do_date" "Do date: ")
                   (form/text-field "do_date" "do date")
                   (form/hidden-field "id" (db/get-next-id))
                   (anti-forgery-field)

                   (form/submit-button "Save order"))]))

(defn new-order [order]
  (db/new-order order))

(new-order {:full_name "GIARDINO" :amount "300" :do_date "23.12.2022"})

;ova forma ce biti koriscena za kreiranje i editovanje narudzbine
;ali po istom principu moze da se koristi za create/update bilo cega
;ovo cu verovatno ovako da pravim za pitanja



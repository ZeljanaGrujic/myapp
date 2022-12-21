(ns new-application.pages)
(require '[hiccup.core :refer [html]])
(require '[hiccup.form :as form])
(require '[new-application.db :as db])

(def all-courses [
                  {:id 0 :name "Ekonomija" :grade "I godina" :smer "ISIT/MNG"}
                  {:id 1 :name "Numericka analiza" :grade "II godina" :smer "ISIT"}
                  {:id 2 :name "Menadzment tehnologije i razvoja" :grade "III godina" :smer "ISIT/MNG"}
                  {:id 3 :name "Modelovanje poslovnih procesa" :grade "IV godina" :smer "ISIT"}])

(defn base-page [& body]
  ;basic template for all our pages
  (html [:head [:title "New user"]]
         [:body [:a {:href "/"}[:h1 "Baza znanja"]]
          body]))

;(defn index [blogs]
;      (base-page
;        (for [b blogs]
;             [:h2 [:a {:href (format "/blogs/%id" (:id b))} (:title b)]])))
(defn cours [c]
  (base-page [:small (:id c)]
             [:h1 (:title c)]
             [:p (:body c)]))

(defn one-cours-view [cours]
  (html [:ul
          (map (fn [[k v]] [:li (format "%s : %s" (name k) (str v))]) cours)]))

(defn cours-view [id]
  (cours (first (db/get-blog-by-id id))))
(cours-view 1)

(defn find-cours-by-id [id]
  (for [c all-courses]
    (if (= id (:id b))
      (cours c))))

(find-cours-by-id 1)

;ova forma ce biti koriscena za kreiranje i prijavljivanje usera
;ali po istom principu moze da se koristi za create/update bilo cega
;ovo cu verovatno ovako da pravim za pitanja
(defn edit-user [u]
  (base-page
    (form/form-to
      [:post (if u (str "/user/" (:id u))
                   "/user")]
      (form/label "username" "Username")
      (form/text-field "username" (:username u))

      (form/label "password" "Password")
      (form/text-area "password" (:password u))

      (form/submit-button "Save!")
      )))

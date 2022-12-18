(ns myapp.pages)
(require '[hiccup.page :refer [html5]])

(defn base-page [& body]
      ;basic template for all our pages
      (html5 [:head [:title "My app Blog"]]
             [:body [:a {:href "/"}[:h1 "Blogs"]]
              body]))

(defn index [blogs]
      (base-page
        (for [b blogs]
             [:h2 [:a {:href (str "/blogs/" (:id b))} (:title b)]])))

(defn blog [b]
      (base-page [:small (:created b)]
                 [:h1 (:title b)]
                 [:p (:body b)]))
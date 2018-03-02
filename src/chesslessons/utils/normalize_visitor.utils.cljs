(ns chesslessons.normalize-visitor.utils)


(def log (.-log js/console))


; ==================
; Private
(defn- -uid [visitor]
    (or
     (aget visitor "uid")
     (if (aget visitor "user") (aget visitor "user" "uid"))))


(defn- -email [visitor]
    (or
     (aget visitor "email")
     (if (aget visitor "user") (aget visitor "user" "email"))))


(defn- -displayName [visitor]
    (or
     (aget visitor "displayName")
     (if (aget visitor "user") (aget visitor "user" "displayName"))
     (if (aget visitor "user") (aget visitor "user" "displayName"))
     (if (aget visitor "additionalUserInfo")
         (aget visitor "additionalUserInfo" "profile" "name"))))


(defn- -photo [visitor]
    (or
     (aget visitor "photoURL")
     (if (aget visitor "user") (aget visitor "user" "photoURL"))
     (if (aget visitor "user")
         (aget visitor "additionalUserInfo" "profile" "picture" "data" "url"))))


(defn- -location [visitor]
    (or
     (if (aget visitor "additionalUserInfo")
         (aget visitor "additionalUserInfo" "profile" "location" "name"))))


(defn- -link [visitor]
    (or
     (if (aget visitor "additionalUserInfo")
         (aget visitor "additionalUserInfo" "profile" "link"))))


(defn -gender [visitor]
    (or
     (if (aget visitor "additionalUserInfo")
         (aget visitor "additionalUserInfo" "profile" "gender"))))


; ==================
; Piblic
(defn normalize_visitor [visitor]
	(let [normalized_visitor {
                      :uid (-uid visitor)
                      :email (-email visitor)
                      :name (-displayName visitor)
                      :photo (-photo visitor)
                      :link (-link visitor)
                      :gender (-gender visitor)
                      }]
		normalized_visitor))


(defn format_visitors [visitors]
    (map (fn [index]
             (let [form_visitor (.data (first (aget visitors "docs")))]
                 (set! (.-key form_visitor) (str index "#"(.-email form_visitor)))
                 (let [result (js->clj form_visitor :keywordize-keys true)]
                     result)
                 )
             ) (range 20)))

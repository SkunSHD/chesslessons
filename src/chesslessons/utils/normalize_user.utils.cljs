(ns chesslessons.normalize-user.utils)


(def log (.-log js/console))


; ==================
; Private
(defn- -uid [user]
	(or
	 (aget user "uid")
	 (if (aget user "user") (aget user "user" "uid"))))


(defn- -email [user]
	(or
	 (aget user "email")
	 (if (aget user "user") (aget user "user" "email"))))


(defn- -displayName [user]
	(or
	 (aget user "displayName")
	 (if (aget user "user") (aget user "user" "displayName"))
	 (if (aget user "user") (aget user "user" "displayName"))
	 (if (aget user "additionalUserInfo") (aget user "additionalUserInfo" "profile" "name"))))


(defn- -photo [user]
	(or
	 (aget user "photoURL")
	 (if (aget user "user") (aget user "user" "photoURL"))
	 (if (aget user "user") (aget user "additionalUserInfo" "profile" "picture" "data" "url"))))


(defn- -location [user]
	(or
	 (if (aget user "additionalUserInfo") (aget user "additionalUserInfo" "profile" "location" "name"))))


(defn- -facebook_link [user]
	(or
	 (if (aget user "additionalUserInfo") (aget user "additionalUserInfo" "profile" "link"))))


(defn- -google_link [user]
	(or
	 (if (aget user "additionalUserInfo") (aget user "additionalUserInfo" "profile" "google_link"))))


(defn -gender [user]
	(or
	 (if (aget user "additionalUserInfo")(aget user "additionalUserInfo" "profile" "gender"))))


; ==================
; Piblic
(defn normalize_user [user]
	(let [normalizeed_user {
                      :uid (-uid user)
                      :email (-email user)
                      :name (-displayName user)
                      :photo (-photo user)
;                      :location (-location user)
                      :facebook_link (-facebook_link user)
                      :google_link (-google_link user)
                      :gender (-gender user)
                      }]
		normalizeed_user))


(defn format_visitors [visitors]
	(map (fn [visitor] (js->clj (.data visitor) :keywordize-keys true))
		 (aget visitors "docs")))

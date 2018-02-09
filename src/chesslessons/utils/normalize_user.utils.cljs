(ns chesslessons.normalize-user.utils)


; ==================
; Private
(defn- -uid [user]
	(or
	 (aget user "uid")
	 (aget user "user" "uid")))


(defn- -email [user]
	(or
	 (aget user "email")
	 (aget user "user" "email")))


(defn- -displayName [user]
	(or
	 (aget user "displayName")
	 (aget user "user" "displayName")
	 (aget user "additionalUserInfo" "profile" "name")))


(defn- -photo [user]
	(or
	 (aget user "photoURL")
	 (aget user "user" "photoURL")
	 (aget user "additionalUserInfo" "profile" "picture" "data" "url")))


(defn- -location [user]
	(or
	 (aget user "additionalUserInfo" "profile" "location" "name")))


(defn- -link [user]
	(or
	 (aget user "additionalUserInfo" "profile" "link")))


(defn -gender [user]
	(or
	 (aget user "additionalUserInfo" "profile" "gender")))

; ==================
; Piblic
(defn normalize_user [user]
	(let [normalizeed_user {
                      :uid (-uid user)
                      :email (-email user)
                      :name (-displayName user)
                      :photo (-photo user)
                      :location (-location user)
                      :link (-link user)
                      :gender (-gender user)
                      }]
		normalizeed_user))
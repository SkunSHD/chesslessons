(ns chesslessons.normalize-user.utils)


; ==================
; Private
(defn- -uid [user] (aget user "uid"))
(defn- -email [user] (aget user "email"))


; ==================
; Piblic
(defn normalize_user [user]
	(let [normalizeed_user {
                      :uid (-uid user)
                      :email (-email user)
                      }]
		normalizeed_user))
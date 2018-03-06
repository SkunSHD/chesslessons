(ns chesslessons.components.admin.admin-visitor.components
	(:require
; 		Utils
		[chesslessons.firebase.db :as db]
		[goog.string :as gstring]
		))

(def log (.-log js/console))


; ==================
; Privat
(defn- -delete_visitor [collection_name uid]
	(case collection_name
		:visitors (db/delete_visitor uid)
		:deleted_visitors (db/delete_visitor_complitly uid)))


(defn- -restore_deleted_visitor [uid]
	(db/restore_deleted_visitor uid))


; ==================
; Public
(defn render_admin_visitor_img_src [visitor]
	(cond
		(not= -1 (.indexOf (:link visitor) "facebook")) "https://cdn3.iconfinder.com/data/icons/free-social-icons/67/facebook_square-48.png"
		(not= -1 (.indexOf (:link visitor) "google")) "https://cdn3.iconfinder.com/data/icons/free-social-icons/67/google_circle_color-48.png"
		:else nil))


(defn render_admin_visitor_link [visitor]
	(if (:link visitor)
		[:a {:href (:link visitor) :cursor "pointer" :target "_blank"}
		 [:img {:src (render_admin_visitor_img_src visitor)}]])

	)


(defn render_added_date [visitor]
	(let [date (new js/Date (:timestamp visitor))]
		(str (.toLocaleDateString date "ru") " " (.getHours date) ":" (.getMinutes date)))
	)


(defn render_date_diff [visitor]
	(let [days_diff (Math/round (/ (- (.getTime (new js/Date)) (:timestamp visitor)) 1000 60 60 24))]
		(if (= days_diff 0) "today" (str days_diff  " day(s) ago"))
		))


(defn render [visitor tab]
	[:li {:key (:email visitor) :style {:position "relative"}}
	 [:img {:src (:photo visitor) :width 50 :height 50}]
	 [:p "email: " (:key visitor)]
	 [:p "name: " (:name visitor)]
	 [:p "Signed up " (render_date_diff visitor) ". (" (render_added_date visitor)")"]
	 [render_admin_visitor_link visitor]
	 [:button.close {:type "button" :aria-label "Close"
					 :style {:position "absolute" :right 0 :top 0}
					 :on-click #(-delete_visitor tab (:uid visitor))}
	  [:span {:aria-hidden "true"} (gstring/unescapeEntities "&times;")]]

	 (if (= tab :deleted_visitors)
		 [:button.btn.btn-secondary {:type "button"
									 :style {:position "absolute" :right 0 :bottom 20}
									 :on-click #(-restore_deleted_visitor (:uid visitor))} "Restore visitor"])
	 [:hr]
	 ])
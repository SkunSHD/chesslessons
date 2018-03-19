(ns chesslessons.components.admin.admin-visitor.component
	(:require
		[reagent.core :refer [atom cursor]]
; 		Utils
		[chesslessons.firebase.db :as db]
		[goog.string :as gstring]
		[clojure.string :as s]
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
		(s/includes? (:link visitor) "facebook") "https://cdn3.iconfinder.com/data/icons/free-social-icons/67/facebook_square-48.png"
		(s/includes? (:link visitor) "google") "https://cdn3.iconfinder.com/data/icons/free-social-icons/67/google_circle_color-48.png"
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


(defn render_admin_visitor_message [visitor]
	(let [read_more_atom (atom false)
		  message        (:visitor_message visitor)
		  is_long_text?  (> (count message) 70)]
		(fn []
			[:p
			 [:span
			  (merge-with into {:style {:paddingRight 10}}
						  (if is_long_text?
							  {:on-click #(reset! read_more_atom (not @read_more_atom))
							   :style    {:cursor "pointer" :color "blue" :text-decoration "underline"}}))
			  "Message:"]

			 (if (and is_long_text? (not @read_more_atom))
				 (str (subs message 0 70) " ...")
				 message)])))


(defn render [visitor tab]
	[:li {:key (:email visitor) :style {:position "relative"}}
	 [:img {:src (:photo visitor) :width 50 :height 50}]
	 [:p "email: " (:email visitor)]
	 [:p "name: " (:name visitor)]
	 [:p "Signed up " (render_date_diff visitor) ". (" (render_added_date visitor)")"]
	 [render_admin_visitor_message visitor]
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
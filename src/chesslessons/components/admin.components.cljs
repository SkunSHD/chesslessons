(ns chesslessons.components.admin.components
	(:require
		[chesslessons.firebase :as fbs]
		[reagent.core :refer [atom cursor]]
;		Models
		[chesslessons.admin-model :as admin_model]
        [chesslessons.visitors-model :as visitors_model]
;		Components
		[chesslessons.components.pagination.components :as pagination_component]
;       Utils
		[chesslessons.firebase.db :as db]
		[goog.string :as gstring]
))


(def log (.-log js/console))


; ==================
; Atoms
(defonce email (atom ""))
(defonce password (atom ""))
(defonce tab (atom :visitors)) ;:visitors :deleted_visitors


; ==================
; Private
(defn- -email_on_change [e]
	(if-not (empty? @admin_model/sign_in_error_msg) (admin_model/set_sign_in_error_msg ""))
	(reset! email (.-value (.-target e))))


(defn- -password_on_change [e]
	(if-not (empty?@admin_model/sign_in_error_msg) (admin_model/set_sign_in_error_msg ""))
	(reset! password (.-value (.-target e))))


(defn- -on_submit_success [e]
	(.preventDefault e)
	(admin_model/sign_in_admin @email @password)
	)

(defn- -on_submit_press [e]
	(if (or (empty? @email) (empty? @password))
		(log "Invalid")
		(-on_submit_success e)
		))

(defn- -delete_visitor [collection_name uid]
	(log collection_name " uid: " uid)
	(case collection_name
		:visitors (db/delete_visitor uid)
		:deleted_visitors (db/delete_visitor_complitly uid)))

(defn- -toggle_active_class [new_el]
	(let [old_el_class_list  (.-classList (.querySelector js/document ".nav-link.active"))]
		(if (.-length old_el_class_list) (do
											 (.remove old_el_class_list "active")
											 (.add (.-classList new_el) "active")))))

(defn- -on_tab_click_handler [event tab_name]
	(.preventDefault event)
	(reset! tab tab_name)
	(-toggle_active_class (.-target event))
	)

(defn- -restore_deleted_visitor [uid]
	(db/restore_deleted_visitor uid))


; ==================
; Components
(defn render_login_form []
	[:form
	 [:h1.h3.mb-3.font-weight-normal "Admin sign in"]

	 [:div.form-group
	  [:label.sr-only "Email address"]
	  [:input.form-control {:placeholder "Email address"
	                        :required true
	                        :on-change -email_on_change
	                        :type "email"}]
	  ]

	 [:div.form-group
	  [:label.sr-only "Password"]
	  [:input.form-control {:placeholder "Password"
	                        :required true
	                        :on-change -password_on_change
	                        :type "password"}]
	  ]

	 (if-not (empty? @admin_model/sign_in_error_msg) [:p @admin_model/sign_in_error_msg])

	 [:button.btn.btn-primary {:on-click -on_submit_press} "Sign in" ]
	 ])


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


(defn render_admin_visitor [visitor]
	[:li {:key (:key visitor) :style {:position "relative"}}
	 [:img {:src (:photo visitor) :width 50 :height 50}]
	 [:p "email: " (:email visitor)]
	 [:p "name: " (:name visitor)]
	 [:p "Signed up " (render_date_diff visitor) ". (" (render_added_date visitor)")"]
	 [render_admin_visitor_link visitor]
	 [:button.close {:type "button" :aria-label "Close"
					 :style {:position "absolute" :right 0 :top 0}
					 :on-click #(-delete_visitor :visitors (:uid visitor))}
	  [:span {:aria-hidden "true"} (gstring/unescapeEntities "&times;")]]
	 [:hr]
	 ])


(defn render_deleted_admin_visitor [visitor]
	[:li {:key (:key visitor) :style {:position "relative"}}
		 [:img {:src (:photo visitor) :width 50 :height 50}]
		 [:p "email: " (:email visitor)]
		 [:p "name: " (:name visitor)]
	     [:p "Signed up " (render_date_diff visitor) ". (" (render_added_date visitor)")"]
		 [render_admin_visitor_link visitor]
		 [:button.close {:type "button" :aria-label "Close"
						:style {:position "absolute" :right 0 :top 0}
						:on-click #(-delete_visitor :deleted_visitors (:uid visitor))}
		 	[:span {:aria-hidden "true"} (gstring/unescapeEntities "&times;")]]
	 	 [:button.btn.btn-secondary {:type "button"
									 :style {:position "absolute" :right 0 :bottom 20}
									 :on-click #(-restore_deleted_visitor (:uid visitor))} "Restore visitor"]
		 [:hr]
	 ])


(defn render_admin_visitors []
	[:ul {:style {:text-align "left" :list-style "none"}}
	 (for [visitor @visitors_model/visitors]
			 ^{:key (:key visitor)} (render_admin_visitor visitor))])


(defn render_admin_deleted_visitors []
	[:ul {:style {:text-align "left" :list-style "none"}}
	 (for [visitor @visitors_model/deleted_visitors]
		 ^{:key (:key visitor)} (render_deleted_admin_visitor visitor))])


(defn reder_tab []
	(case @tab
		:visitors [render_admin_visitors]
		:deleted_visitors [render_admin_deleted_visitors]
		nil))


(defn render_navigation [child]
	[:div.card.text-center
	 [:div.card-header
	  [:ul.nav.nav-tabs.card-header-tabs
	   [:li.nav-item
		[:a.nav-link.active {:on-click #(-on_tab_click_handler % :visitors)} "New"]]
	   [:li.nav-item
		[:a.nav-link {:on-click #(-on_tab_click_handler % :deleted_visitors)} "Deleted"]]]]
	 [:div.card-body
	  [child]]
	 ])


(defn render_admin_container []
	 [:div
	   	[:h1 "Visitors:"]
	  	(render_navigation reder_tab)
	  [pagination_component/render]
	  ])

(ns chesslessons.components.admin.components
	(:require
		[reagent.core :refer [atom cursor]]
;		Models
		[chesslessons.admin-model :as admin_model]
        [chesslessons.visitors-model :as visitors_model]
;		Components
		[chesslessons.components.pagination.component :as pagination_component]
		[chesslessons.components.admin.admin-visitor.components :as visitor_component]
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


(defn render_admin_visitors []
	[:ul {:style {:text-align "left" :list-style "none"}}
	 (let [tab_key @tab]
		 (for [visitor (visitors_model/get_current_page_visitors tab_key)]
			 ^{:key (:key visitor)} (visitor_component/render visitor tab_key))
		 )
	 ])


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


; ==================
; Components
(defn render_admin_container []
	[:div
	 [:h1 "Visitors:"]
	 [render_navigation render_admin_visitors]
	 [pagination_component/render tab]
	 ])


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

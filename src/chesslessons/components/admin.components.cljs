(ns chesslessons.components.admin.components
	(:require
		[reagent.core :refer [atom cursor]]
;		Models
		[chesslessons.search-model :refer [search is_searching search_visitors]]
		[chesslessons.admin-model :as admin_model]
		[chesslessons.visitors-model :as visitors_model]
;		Components
		[chesslessons.components.pagination.component :as pagination_component]
		[chesslessons.components.admin_search.component :refer [render_admin_search]]
		[chesslessons.components.admin.admin-visitor.component :as visitor_component]
))


(def log (.-log js/console))


; ==================
; Atoms
(defonce email (atom ""))
(defonce password (atom ""))
(defonce tab (atom :all_visitors)) ;:visitors :deleted_visitors


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


(defn- -toggle_active_class [new_el toggledClass]
	(let [old_elem_query (.querySelector js/document toggledClass)]
		(if old_elem_query
			(let [same_el? (= (.-innerText new_el) (.-innerText old_elem_query))]
				(.remove (.-classList old_elem_query) "active")
				(if-not same_el? (.add (.-classList new_el) "active"))
				)
			(.add (.-classList new_el) "active"))))


(defn- -on_tab_click_handler [event tab_name]
	(.preventDefault event)
	(reset! tab tab_name)
	(-toggle_active_class (.-target event) ".nav-link.active")
	)


(defn- -on_filter_click_handler [event tab_name]
	(.preventDefault event)
	(reset! tab (if (and (not= @tab :all_visitors) (= @tab tab_name)) :all_visitors tab_name))
	(-toggle_active_class (.-target event) ".filter-link.active")
	)


(defn render_admin_visitors []
	[:ul {:style {:text-align "left" :list-style "none"}}
	 (let [tab_key @tab visitors (if (is_searching)
									 (search_visitors tab_key)
									 (visitors_model/get_current_page_visitors tab_key))]
		 (for [visitor visitors]
			 (visitor_component/render visitor tab_key))
		 )
	 ])


(defn render_filters []
	[:div "Visitors filter:"
		[:button.btn.btn-info.filter-link {:on-click #(-on_filter_click_handler % :visitors)} "Social Network"]
		[:button.btn.btn-info.filter-link {:on-click #(-on_filter_click_handler % :anonymous_visitors)} "Anonymous"]
	 	[render_admin_search]
	 ]
	)


(defn render_tab_and_visitors_container []
	[:div.card.text-center
	 [:div.card-header
	  [:ul.nav.nav-tabs.card-header-tabs
	   [:li.nav-item
		[:a.nav-link.active {:on-click #(-on_tab_click_handler % :all_visitors)} "All visitors"]]
	   [:li.nav-item
		[:a.nav-link {:on-click #(-on_tab_click_handler % :deleted_visitors)} "Deleted"]]]
	  ]
	 [:div.card-body
	  [render_filters]
	  [render_admin_visitors]]
	 ])


; ==================
; Components
(defn render_admin_container []
	[:div
	 [:h1 "Visitors:"]
	 [render_tab_and_visitors_container]
     (when (not (is_searching)) [pagination_component/render tab])
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

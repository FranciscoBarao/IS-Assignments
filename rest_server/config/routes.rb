Rails.application.routes.draw do
  get 'list/:type', to: 'information#index'
  resources :information
  get 'results/revenue', to: 'results#revenue'
  get 'results/expense', to: 'results#expense'
  get 'results/profit', to: 'results#profit'
  get 'results/total_revenue', to: 'results#total_revenue'
  get 'results/total_expense', to: 'results#total_expense'
  get 'results/total_profit', to: 'results#total_profit'
  get 'results/mean_per_item', to: 'results#mean_per_item'
  get 'results/mean_per_purchase', to: 'results#mean_per_purchase'
  get 'results/total_revenue_window', to: 'results#total_revenue_window'
  get 'results/total_expense_window', to: 'results#total_expense_window'
  get 'results/total_profit_window', to: 'results#total_profit_window'
  get 'results/highest_profit', to: 'results#highest_profit'
  get 'results/highest_sales', to: 'results#highest_sales'

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end

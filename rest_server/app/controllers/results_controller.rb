class ResultsController < ApplicationController
  before_action :set_result

  def revenue
    @results = Result.where(data_type: 'revenue').uniq_by(&:information_id)
  end

  def expense
    @results = Result.where(data_type: 'expense').uniq_by(&:information_id)
  end

  def profit
    @results = Result.where(data_type: 'profit').uniq_by(&:information_id)
  end

  def total_revenue
    @result = Result.where(data_type: 'totalRevenue').last
  end

  def total_expense
    @result = Result.where(data_type: 'totalExpense').last
  end

  def total_profit
    @result = Result.where(data_type: 'totalProfit').last
  end

  def mean_per_item
    @results = Result.where(data_type: 'medianPerItem').uniq_by(&:information_id)
  end

  def mean_per_purchase
    @result = Result.where(data_type: 'totalMedian').last
  end

  def total_revenue_window
    @result = Result.where(data_type: 'totalWindowRevenue').last
  end

  def total_expense_window
    @result = Result.where(data_type: 'totalWindowExpense').last
  end

  def total_profit_window
    @result = Result.where(data_type: 'totalWindowProfit').last
  end

  def highest_profit
    @result = Result.where(data_type: 'highestProfit').last
  end

  def highest_sales
    @result = Result.where(data_type: 'highestSales').uniq_by(&:information_id)
  end

  private
    # Never trust parameters from the scary internet, only allow the white list through.
    def result_params
      params.permit(:data_type, :value, :information_id)
    end
end

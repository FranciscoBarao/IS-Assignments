class ResultsController < ApplicationController

  def revenue
    # @results = Result.where(data_type: 'revenue').order(:id).uniq(&:information_id)
    name = 'revenue'
    results = Result.where(data_type: name).pluck(:information_id).uniq
    @results = []
    results.each do |id|
      @results << Result.where(data_type: name, information_id: id).order(:id).last
    end
  end

  def expense
    name = 'expense'
    results = Result.where(data_type: name).pluck(:information_id).uniq
    @results = []
    results.each do |id|
      @results << Result.where(data_type: name, information_id: id).order(:id).last
    end
  end

  def profit
    name = 'profit'
    results = Result.where(data_type: name).pluck(:information_id).uniq
    @results = []
    results.each do |id|
      @results << Result.where(data_type: name, information_id: id).order(:id).last
    end
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
    name = 'medianPerItem'
    results = Result.where(data_type: name).pluck(:information_id).uniq
    @results = []
    results.each do |id|
      @results << Result.where(data_type: name, information_id: id).order(:id).last
    end
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
    name = 'highestSales'
    results = Result.where(data_type: name).pluck(:information_id).uniq
    @results = []
    results.each do |id|
      @results << Result.where(data_type: name, information_id: id).order(:id).last
    end
  end

  private
    # Never trust parameters from the scary internet, only allow the white list through.
    def result_params
      params.permit(:data_type, :value, :information_id)
    end
end

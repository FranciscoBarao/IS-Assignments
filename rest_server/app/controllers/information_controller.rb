class InformationController < ApplicationController
  before_action :set_information, only: [:show, :update, :destroy]

  # GET /information
  def index
    @information = Information.all

    render json: @information
  end

  # GET /information/1
  def show
    render json: @information
  end

  # POST /information
  def create
    @information = Information.new(information_params)

    if @information.save
      render json: @information, status: :created, location: @information
    else
      render json: @information.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /information/1
  def update
    if @information.update(information_params)
      render json: @information
    else
      render json: @information.errors, status: :unprocessable_entity
    end
  end

  # DELETE /information/1
  def destroy
    @information.destroy
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_information
      @information = Information.find(params[:id])
    end

    # Only allow a trusted parameter "white list" through.
    def information_params
      params.require(:information).permit(:type, :name)
    end
end

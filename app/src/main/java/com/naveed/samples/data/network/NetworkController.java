package com.naveed.samples.data.network;

import org.greenrobot.eventbus.EventBus;

import com.naveed.samples.apputils.events.SingleDataEvent;
import com.naveed.samples.data.models.ListMoviesResp;
import com.naveed.samples.helper.utils.AppConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkController {
    private static NetworkController sNetworkController;
    private final String unknownError = "Unknown Error Occurred";


    public static NetworkController getInstance() {
        if (sNetworkController == null) {
            sNetworkController = getNewInstance();
        }
        return sNetworkController;
    }

    private synchronized static NetworkController getNewInstance() {
        if (sNetworkController != null)
            return sNetworkController;
        return new NetworkController();
    }

    private NetworkController() {

    }

    //
    public void getMoviesList(String listType, int pageIndex) {
        Call<ListMoviesResp> call = MoviesServiceFactory.getInstance().getMoviesList(listType, AppConstants.API_KEY_IMDB, AppConstants.LANG, pageIndex, "");
        call.enqueue(new Callback<ListMoviesResp>() {
            @Override
            public void onResponse(Call<ListMoviesResp> call, Response<ListMoviesResp> response) {
                if (response.isSuccessful()) {
                    ListMoviesResp resp = response.body();
                    postEventSingleResponse(true, EventBusKeys.MOVIE_LIST, "", resp);
                } else {
                    postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, unknownError, null);

                }
            }

            @Override
            public void onFailure(Call<ListMoviesResp> call, Throwable t) {
                postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, t.getMessage(), null);
            }
        });
    }

    public void getSearchMoviesList(String query, int pageIndex) {
        Call<ListMoviesResp> call = MoviesServiceFactory.getInstance().getSearchAbleResultsMoviesList(AppConstants.API_KEY_IMDB, query, pageIndex);
        call.enqueue(new Callback<ListMoviesResp>() {
            @Override
            public void onResponse(Call<ListMoviesResp> call, Response<ListMoviesResp> response) {
                if (response.isSuccessful()) {
                    ListMoviesResp resp = response.body();
                    postEventSingleResponse(true, EventBusKeys.MOVIE_LIST, "", resp);
                } else {
                    postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, unknownError, null);

                }
            }

            @Override
            public void onFailure(Call<ListMoviesResp> call, Throwable t) {
                postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, t.getMessage(), null);
            }
        });
    }


    public void postEventSingleResponse(boolean status, int responseId, String message, Object data) {
        SingleDataEvent eventBusData = new SingleDataEvent(status, responseId, message, data);
        EventBus.getDefault().post(eventBusData);
    }

}

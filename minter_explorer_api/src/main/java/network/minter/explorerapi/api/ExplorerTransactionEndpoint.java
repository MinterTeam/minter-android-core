/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.explorerapi.api;

import java.util.List;
import java.util.Map;

import network.minter.explorerapi.models.ExpResult;
import network.minter.explorerapi.models.HistoryTransaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface ExplorerTransactionEndpoint {

    @GET("/api/v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@QueryMap Map<String, String> query);

    @GET("/api/v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@Query("addresses[]") List<String> addresses);

    @GET("/api/v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@Query("addresses[]") List<String> addresses, @Query("page") long page);
}

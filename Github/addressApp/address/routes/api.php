<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

//I did not add authentication access because it was a sample app which did not require any authorization token


Route::group([
  'prefix' => 'auth'
], function () {
  //routes go here

  Route::resource('contacts', 'ContactController');


});

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

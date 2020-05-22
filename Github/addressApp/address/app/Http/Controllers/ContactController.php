<?php

namespace App\Http\Controllers;
use\App\Contact;
use Image;
use Illuminate\Http\Request;

class ContactController extends Controller
{
  public function index(){

    $contacts = Contact::all();


    return response()->json([
          'success' => true,
          'data' => $contacts->toArray(),
          'message' => 'Contacts loaded successfully'
      ], 200);



  }

  //this function is used to display one
  public function show($id){


    if(Contact::where('id', $id)->exists()){

      $contact = Contact::find($id);


      return response()->json([
            'success' => true,
            'data' => $contact->toArray(),
            'message' => 'Item loaded successfully'
        ], 200);

    } else {

      return response()->json([
               'success' => false,
               'message' => 'Contact with id ' . $id . 'not found'
            ], 404);

    }



  }

  //this function stores the contacts in the server
  public function store(Request $request) {


    $this->validate($request, [
      'firstname' => 'required',
      'lastname' => 'required',
      'email' => 'required|email',
      'phone' => 'required',
    ]);

    $contact = new Contact();
    $contact->firstname = $request->firstname;
    $contact->lastname = $request->lastname;
    $contact->email = $request->email;
    $contact->phone = $request->phone;


      if($contact->save()){

        return response()->json([
              'success' => true,
              'data' => $contact->toArray(),
              'message' => 'Contact saved successfully'
          ], 200);


      } else {


        return response()->json([
              'success' => false,
              'message' => 'Contact could not be added '
          ], 404);


      }


  }

  //this is to update the contact uploaded to the server
  public function update(Request $request, $id) {

    $image = null;


    if(Contact::where('id', $id)->exists()){

      $contact = Contact::find($id);

      $contact->firstname = is_null($request->firstname) ? $contact->firstname : $request->firstname;
      $contact->lastname = is_null($request->lastname) ? $contact->lastname : $request->lastname;
      $contact->email = is_null($request->email) ? $contact->email : $request->email;
      $contact->phone = is_null($request->phone) ? $contact->phone : $request->phone;

      if($contact->save()){


        return response()->json([
              'success' => true,
              'data' => $contact->toArray(),
              'message' => 'Contact updated successfully'
          ], 200);

      } else {

        return response()->json([
              'success' => false,
              'message' => 'Contact could not be updated'
          ], 404);


      }


    } else {


      return response()->json([
              'success' => false,
              'message' => 'Contact with id ' . $id . 'not found'
          ], 404);

    }

  }


  //this function deletes an entry in the server
  public function destroy($id) {


    if(Contact::where('id', $id)->exists()){

      $contact = Contact::find($id);


      if($contact->delete()) {

        return response()->json([
              'success' => true,
              'message' => 'Contact deleted successfully'
          ], 200);


      } else {

        return response()->json([
              'success' => false,
              'message' => 'Contact could not be deleted '
          ], 404);


      }


    } else {

      return response()->json([
               'success' => false,
               'message' => 'Contact with id ' . $id . 'not found'
           ], 404);


    }


  }

}

package controllers;

import connectors.BackendConnector;
import forms.MessageInputForm;
import models.MessageInput;
import org.json.JSONException;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.mvc.Result;
import javax.inject.Inject;
import javax.inject.Singleton;

import static play.mvc.Controller.flash;
import static play.mvc.Controller.session;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import play.Logger;

import java.util.List;

@Singleton
public class MessageController {

    private final Form<MessageInputForm> form;


    @Inject
    public MessageController(FormFactory formFactory) {
        this.form = formFactory.form(MessageInputForm.class);
    }

    public Result submitMessage() throws JSONException {

        Logger.debug("submit message");
        final Form<MessageInputForm> boundForm = form.bindFromRequest();

        if (boundForm.hasErrors()) {
            String errorMsg = "";
            java.util.Map<String, List<ValidationError>> errorsAll = boundForm.errors();
            for (String field : errorsAll.keySet()) {
                errorMsg += field + " ";
                for (ValidationError error : errorsAll.get(field)) {
                    errorMsg += error.message() + ", ";
                }
            }
            flash("globalError", errorMsg);
            return ok(views.html.homepage.render(boundForm, ""));
        } else {
            MessageInputForm data = boundForm.get();
            String response = BackendConnector.sendMessage(new MessageInput(data.getMessage()));
            return ok(views.html.homepage.render(form, response));
        }
    }



    public Result getHomepage() {
        session().clear();
        return ok(views.html.homepage.render(form,""));
    }
}

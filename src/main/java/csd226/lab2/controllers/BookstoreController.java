package csd226.lab2.controllers;

import csd226.lab2.data.Account;
import csd226.lab2.data.Content;
import csd226.lab2.data.Registry;
import csd226.lab2.repositories.AccountRepository;
import csd226.lab2.repositories.RegistryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookstoreController {

    @GetMapping("/publiccontent")
    public ResponseEntity<String> getPublicContent() {
        return ResponseEntity.ok(getRegistry("public_content"));
    }

    @GetMapping("/staffcontent")
    public ResponseEntity<String> getStaffContent() {
        return ResponseEntity.ok(getRegistry("staff_content"));
    }


    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok(
                "getAdmin() : Admin" +
                        // a button that edit public content
                        "<form hx-get=\"/publicedit\" hx-target=\"this\" hx-swap=\"outerHTML\">\n" +
                        "    <button class=\"btn\"><span class=\"glyphicon glyphicon-edit\"></span> Edit Public Content</button>\n" +
                        "</form>" +
                        // a button that edit staff content
                        "<form hx-get=\"/staffedit\" hx-target=\"this\" hx-swap=\"outerHTML\">\n" +
                        "    <button class=\"btn\"><span class=\"glyphicon glyphicon-edit\"></span> Edit Staff Content</button>\n" +
                        "</form>");
    }

    @GetMapping("/student")
    public ResponseEntity<String> getStudent(){
        return ResponseEntity.ok("getStudent() : Student");
    }

    @GetMapping("/publicedit")
    public ResponseEntity<String> getPublicEdit(){
        return ResponseEntity.ok("getPublicEdit() : PublicEdit"
                + "<form hx-post=\"/publiccontent\" hx-target=\"this\" hx-swap=\"outerHTML\">\n" +
                "    <div>\n" +
                "        <label>Content</label>\n" +
                "        <textarea name=\"content\" rows=\"5\" cols=\"30\">Hello World</textarea>\n" +
                "    </div>\n" +
                "    <button class=\"btn\">Submit</button>\n" +
                "    <button class=\"btn\">Cancel</button>\n" +
                "</form>");
    }

    @GetMapping("/staffedit")
    public ResponseEntity<String> getStaffEdit(){
        return ResponseEntity.ok("getStaffEdit() : StaffEdit"
                + "<form hx-post=\"/staffcontent\" hx-target=\"this\" hx-swap=\"outerHTML\">\n" +
                "    <div>\n" +
                "        <label>Content</label>\n" +
                "        <textarea name=\"content\" rows=\"5\" cols=\"30\">Hello World</textarea>\n" +
                "    </div>\n" +
                "    <button class=\"btn\">Submit</button>\n" +
                "    <button class=\"btn\">Cancel</button>\n" +
                "</form>");
    }

    @PutMapping("/publiccontent")
    public ResponseEntity<Boolean> savePublicContent(@RequestBody @Valid Registry content) {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/staffcontent")
    public ResponseEntity<Boolean> saveStaffContent(@RequestBody @Valid Registry content) {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }

    @Autowired
    RegistryRepository registryRepository;

    private Boolean updateRegistry(String registryKey, String registryValue) {
        //Find the record for the registry entry based on the supplied key
        List<Registry> registryEntries = registryRepository.findByRegistryKey(registryKey);

        Registry registryEntry = new Registry();

        if (registryEntries.size() == 0) {
            registryEntry.setRegistryKey(registryKey);
        } else {
            registryEntry = registryEntries.get(0);
        }

        registryEntry.setRegistryValue(registryValue);

        //Update the registry table with new value
        registryRepository.save(registryEntry);

        return true;
    }

    private String getRegistry(String registryKey) {
        //Find the record for the registry entry based on the supplied key
        List<Registry> registryEntries = registryRepository.findByRegistryKey(registryKey);

        Registry registryEntry = new Registry();

        if (registryEntries.size() == 0) {
            return "";
        }

        return registryEntries.get(0).getRegistryValue();
    }

}

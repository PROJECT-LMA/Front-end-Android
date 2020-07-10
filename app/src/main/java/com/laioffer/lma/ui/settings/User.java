package com.laioffer.lma.ui.settings;

public class User {
    private String objectId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private User(UserBuilder user) {
        this.objectId = user.objectId;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.password = user.password;
    }

    public class UserBuilder {
        private String objectId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;


        private void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        private void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        private void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public User build() {
            return new User(this);
        }

    }

}

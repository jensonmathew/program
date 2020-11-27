## Feature Flags
As part of this application,
- Added these Feature Flags strategies
  - [All users](https://docs.gitlab.com/ee/operations/feature_flags.html#all-users) : awesomefeature
  - [User IDs](https://docs.gitlab.com/ee/operations/feature_flags.html#user-ids) : useridfeature
  - [Environment Specific](https://docs.gitlab.com/ee/operations/feature_flags.html#disable-a-feature-flag-for-a-specific-environment) : envfeature

![List_of_Feature_Flags](./images/List_of_Feature_Flags.PNG)

## How Has This Been Tested?
**All users** Feature Flag validation:-
- http://cdp-helloworld-1-development.dev.px-npe03a.cf.t-mobile.com
- User & Password details

| User Name | Password |
| --- | --- |
| testuser1, testuser2, testuser3 ... testuser10 | password1, password2, password3 ... password10 |

![All_Users_Feature_Flag](./images/All_Users_Feature_Flag.PNG)

**User IDs** Feature Flag validation:-
- This Feature Flag is enabled for some specific users(testuser1, testuser3, testuser5, testuser7, testuser9)
- http://cdp-helloworld-1-development.dev.px-npe03a.cf.t-mobile.com
- User & Password details

| User Name | Password |
| --- | --- |
| testuser1, testuser3, testuser5 ... testuser9 | password1, password3, password5 ... password9 |

![User_IDs_Feature_Flag](./images/User_IDs_Feature_Flag.PNG)

**Environment Specific** Feature Flag validation:-
- This Feature Flag is enabled for some specific environment where application deployed(dev01/px-npe02a/development/smoke, dev01/px-npe02c/development/smoke)
- http://cdp-helloworld-1-development.dev.px-npe02a.cf.t-mobile.com
- http://cdp-helloworld-1-development.dev.px-npe02c.cf.t-mobile.com
- Any users can be login to these apps

![Env_Specific_Feature_Flag](./images/Env_Specific_Feature_Flag.PNG)
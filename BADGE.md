# Badges

By default, Giblab only provides two badges which are Pipeline and Coverage.  You can extend that by using functionality from [Shields.io](https://shields.io/)

There are two types available.

## Static

![badge](https://img.shields.io/badge/slack-%23cdp--support-magenta?logo=slack)
You can create a static badge whose value never changes like this
``https://img.shields.io/badge/slack-%23cdp--support-magenta?logo=slack``
See [Shields.io](https://shields.io/) for full documentaion but the basic format is
``https://img.shields.io/badge/<LABEL>-<MESSAGE>-<COLOR>``

## Dynamic

This one requires a bit more work but also provides much greater flexibility.
You will be creating a json file to utilize the enpoint functionality documented [here](https://shields.io/endpoint).

### Configure pages

1. Go to Settings -> General -> Visibility, project features, permissions and click expand.
*If you don't see the gear icon or the Settings menu option on the left side of your project repo then you don't have enough permissions to proceed.*
1. Scroll down to Pages and ensure it is enabled and that the drop down is set to Everyone.
**Warning: This makes the content available to the public internet so do not include sensitive content in your json!**
1. Click the Save changes button.

### Create template

1. Create a file.  It can be called anything but the suggestion is badge.template to clearly indicate its function. This file can exist anywhere but the preferred location is .gitlab.  Technically it is not required at all but will make creating the json file easier.
1. Create the json using this as an example.  Additional information can be found at [Shields.io](https://shields.io/endpoint).
``{
  "schemaVersion": 1,
  "label": "hello",
  "message": "sweet world",
  "color": "orange"
}``

### Modify pipeline

You need to modify the pipeline to create the json file and make it publicly available.
The json content is only limited by your creativity, but here is the basic concept.

```YAML
pages:
  image: alpine:latest
  script:
    - apk add jq git
    - CONTRIB_COUNT=`git shortlog -s origin/tmo/master | wc -l`
    - jq --arg COUNT $CONTRIB_COUNT '.message=$COUNT' .gitlab/badge.template > contribute.json
    - mkdir -p ./public
    - cp ./*.json ./public/
  artifacts:
    paths:
    - public
    expire_in: 90 days
  only:
    - tmo/master
```

- The section must be named pages.  This is a GitLab requirement.
- You can use whatever image you prefer.  alpine:latest is lightweight but may not contain all the programs you need.
- The script is where the actual work goes to create the json file. You must make a public folder and all json files must be in the public folder.
- The artifacts block is required, and the path must be public.  expire_in is optional and can be be set to your preference.
- The only block is optional but you probably only want this to run on a merge to master

### Configure repository

1. Go to Settings -> Pages. This will give you the URL where your content is located.  As noted it could take up to 30 minutes after the pipeline runs before content is available the first time, however it is usually between 5 - 10 minutes.
1. Go to Settings -> General -> Badges and click expand
1. Enter a Name.  It is just for configuration and will not be displayed with the badge.
1. Enter a link to a valid URL where you want the badge to go when clicked.
1. Enter the badge image URL. For example ``https://img.shields.io/endpoint?url=https://tmobile.gitlab.io/templates_projects/helloworld-1/contribute.json``
   It will be in this format ``https://img.shields.io/endpoint?url=<URL from step 1>/<file name you created in your pipeline>``
1. If you did everything correctly you will now see a preview of your image.
1. Click the Save changes button.

# Raven Email Agents

These are third party agents used to send and receive emails

| Agent    | url images | inline images | attachments |
|----------|:----------:|:-------------:|:-----------:|
| brevo    |  partial   |      no       |     no      |
| console  |     no     |      no       |     no      |
| mailgun  |    yes     |      no       |   partial   |
| postmark |    yes     |      yes      |     yes     |

Key
- partial: Support is on few clients

## Gradle Setup
```groovy
dependencies {
    implementation("tz.co.asoft:raven-email-agent-brevo") // for brevo agent
    implementation("tz.co.asoft:raven-email-agent-console") // for console agent
    implementation("tz.co.asoft:raven-email-agent-mailgun") // for mailgun agent
    implementation("tz.co.asoft:raven-email-agent-postmark") // for postmark agent
}
```

## AddOns
To attach file resources, add
```groovy
dependencies {
    implementation("tz.co.asoft:raven-resources-file")
}
```
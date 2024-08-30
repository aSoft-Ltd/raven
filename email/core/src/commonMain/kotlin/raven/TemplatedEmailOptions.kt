package raven

class TemplatedEmailOptions(
    val factory: (params: FactoryParams) -> SendEmailTemplateParams
)
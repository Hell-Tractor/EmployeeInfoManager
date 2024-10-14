export const rules = {
    required: (value: any) => !!value || '必填',
    username: (value: any) => /^[0-9a-zA-Z_]{4,16}$/.test(value) || '用户名格式错误',
    password: (value: any) => /^[0-9a-zA-Z_]{4,16}$/.test(value) || '密码格式错误',

    depart: (value: any) => /^\S{2,16}$/.test(value) || '公司名称格式错误',
    riskTag: (value: any) => /^\S{1,8}$/.test(value) || '标签名称格式错误',
    personId: (value: any) => /^\d{18}$/.test(value) || '身份证号格式错误',
}
export const rules = {
    required: (value: any) => !!value || '必填',
    username: (value: any) => /^[0-9a-zA-Z_]{4,16}$/.test(value) || '用户名格式错误(4-16个数字/字母/下划线)',
    password: (value: any) => /^[0-9a-zA-Z_]{4,16}$/.test(value) || '密码格式错误(4-16个数字/字母/下划线)',

    depart: (value: any) => /^\S{2,16}$/.test(value) || '公司名称格式错误(2-16个非空白字符)',
    riskTag: (value: any) => /^\S{1,8}$/.test(value) || '标签名称格式错误(1-8个非空白字符)',
    personId: (value: any) => /^\d{17}(\d|X)$/.test(value) || '身份证号格式错误',
    projectName: (value: any) => /^\S{1,16}$/.test(value) || '项目名称格式错误(1-16个非空白字符)',
    name: (value: any) => /^\S{1,6}$/.test(value) || '姓名格式错误(1-6个非空白字符)',
    age: (value: number) => (value >= 18 && value <= 100) || '年龄不合法',

    maxLength128: (value: any) => (value.length <= 128) || '最多128个字符',
}
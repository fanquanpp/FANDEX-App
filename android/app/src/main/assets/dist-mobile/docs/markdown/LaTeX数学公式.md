# LaTeX 数学公式

> **符号约定**：`< >` 必填参数 | `[ ]` 可选参数

---

## 行内公式

**行内公式：使用单个 $ 包裹**
`$<公式>$`
```markdown
质能方程 $E = mc^2$ 是物理学最著名的公式之一。
```

---

## 块级公式

**块级公式：使用双 $$ 包裹**
`$$\n<公式>\n$$`
```markdown
$$
E = mc^2
$$
```

---

## 上标与下标

**上标：使用 ^ 符号**
`$<底>^<指数>$`
```markdown
$x^2$
$x^{10}$
```

**下标：使用 _ 符号**
`$<底>_<下标>$`
```markdown
$a_n$
$a_{ij}$
```

**上下标组合**
`$<底>_<下标>^<指数>$`
```markdown
$x_1^2$
```

---

## 分数

**分数：使用 \frac**
`$\frac{<分子>}{<分母>}$`
```markdown
$\frac{a}{b}$
$\dfrac{a}{b}$（大分数）
```

**连分数**
`$\cfrac{<分子>}{<分母>}$`
```markdown
$\cfrac{1}{1+\cfrac{1}{1+\cfrac{1}{1}}}$
```

---

## 根号

**平方根**
`$\sqrt{<表达式>}$`
```markdown
$\sqrt{2}$
```

**n 次方根**
`$\sqrt[<n>]{<表达式>}$`
```markdown
$\sqrt[3]{8}$
$\sqrt[n]{a}$
```

---

## 希腊字母

**小写希腊字母**
`$\<命令>$`
```markdown
$\alpha$   \alpha
$\beta$    \beta
$\gamma$   \gamma
$\delta$   \delta
$\epsilon$ \epsilon
$\theta$   \theta
$\lambda$  \lambda
$\mu$      \mu
$\pi$      \pi
$\sigma$   \sigma
$\omega$   \omega
$\phi$     \phi
```

**大写希腊字母**
`$\<命令>$`
```markdown
$\Gamma$   \Gamma
$\Delta$   \Delta
$\Theta$   \Theta
$\Lambda$  \Lambda
$\Pi$      \Pi
$\Sigma$   \Sigma
$\Omega$   \Omega
$\Phi$     \Phi
```

---

## 求和与积分

**求和**
`$\sum_{<下界>}^{<上界>} <表达式>$`
```markdown
$\sum_{i=1}^{n} i$
```

**乘积**
`$\prod_{<下界>}^{<上界>} <表达式>$`
```markdown
$\prod_{i=1}^{n} i$
```

**积分**
`$\int_{<下界>}^{<上界>} <表达式> d<变量>$`
```markdown
$\int_{0}^{\infty} f(x) dx$
$\iint_{D} f(x,y) dA$
$\oint_{C} F \cdot dr$
```

---

## 极限与导数

**极限**
`$\lim_{<变量> \to <值>} <表达式>$`
```markdown
$\lim_{x \to \infty} f(x)$
```

**导数**
`$\frac{d<因变量>}{d<自变量>}$`
```markdown
$\frac{dy}{dx}$
```

**偏导数**
`$\frac{\partial <函数>}{\partial <变量>}$`
```markdown
$\frac{\partial f}{\partial x}$
```

**梯度**
`$\nabla <函数>$`
```markdown
$\nabla f$
```

---

## 关系运算符

**关系运算符**
`$\<命令>$`
```markdown
$\leq$      小于等于
$\geq$      大于等于
$\neq$      不等于
$\approx$   约等于
$\equiv$    恒等于
$\in$       属于
$\subset$   真子集
$\subseteq$ 子集
$\forall$   任意
$\exists$   存在
```

---

## 矩阵

**圆括号矩阵**
`\begin{pmatrix} ... \end{pmatrix}`
```markdown
$$
\begin{pmatrix}
a & b \\
c & d
\end{pmatrix}
$$
```

**方括号矩阵**
`\begin{bmatrix} ... \end{bmatrix}`
```markdown
$$
\begin{bmatrix}
1 & 2 & 3 \\
4 & 5 & 6 \\
7 & 8 & 9
\end{bmatrix}
$$
```

**行列式**
`\begin{vmatrix} ... \end{vmatrix}`
```markdown
$$
\begin{vmatrix}
a & b \\
c & d
\end{vmatrix} = ad - bc
$$
```

**增广矩阵**
`\left[\begin{array}{cc|c} ... \end{array}\right]`
```markdown
$$
\left[
\begin{array}{cc|c}
1 & 2 & 3 \\
4 & 5 & 6
\end{array}
\right]
$$
```

---

## 方程组与分段函数

**方程组**
`\begin{cases} ... \end{cases}`
```markdown
$$
\begin{cases}
x + y = 5 \\
2x - y = 1
\end{cases}
$$
```

**分段函数**
`f(x) = \begin{cases} ... \end{cases}`
```markdown
$$
f(x) = \begin{cases}
x^2 & \text{if } x \geq 0 \\
-x^2 & \text{if } x < 0
\end{cases}
$$
```

---

## 字体控制

**数学字体**
`$\<命令>{<文本>}$`
```markdown
$\mathbf{A}$    粗体
$\mathbb{R}$    黑板粗体
$\mathcal{L}$   花体
$\mathsf{A}$    无衬线
$\mathtt{A}$    打字机
$\text{文本}$   正体文本
```

---

## 空格控制

**空格命令**
`$\<命令>$`
```markdown
$a\!b$      负空格
$ab$        无空格
$a\,b$      薄空格
$a\;b$      中等空格
$a\quad b$  1em 空格
$a\qquad b$ 2em 空格
```

---

## 颜色

**文字颜色**
`$\textcolor{<颜色>}{<文本>}$`
```markdown
$\textcolor{red}{红色文字}$
$\textcolor{blue}{蓝色文字}$
```

---

## 常见公式示例

**欧拉公式**
`$e^{i\pi} + 1 = 0$`
```markdown
$$
e^{i\pi} + 1 = 0
$$
```

**高斯积分**
`$\int_{-\infty}^{\infty} e^{-x^2} dx = \sqrt{\pi}$`
```markdown
$$
\int_{-\infty}^{\infty} e^{-x^2} dx = \sqrt{\pi}
$$
```

**贝叶斯定理**
`$P(A|B) = \frac{P(B|A) \cdot P(A)}{P(B)}$`
```markdown
$$
P(A|B) = \frac{P(B|A) \cdot P(A)}{P(B)}
$$
```

/*
 * Copyright (C) 2018-2020. Huawei Technologies Co., Ltd. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.utils;

import io.prestosql.Session;
import io.prestosql.SystemSessionProperties;
import io.prestosql.sql.analyzer.FeaturesConfig;
import io.prestosql.sql.builder.optimizer.SubQueryPushDown;
import io.prestosql.sql.planner.SimplePlanVisitor;
import io.prestosql.sql.planner.iterative.IterativeOptimizer;
import io.prestosql.sql.planner.iterative.Rule;
import io.prestosql.sql.planner.iterative.rule.HintedReorderJoins;
import io.prestosql.sql.planner.iterative.rule.PushLimitThroughOuterJoin;
import io.prestosql.sql.planner.iterative.rule.PushLimitThroughSemiJoin;
import io.prestosql.sql.planner.iterative.rule.PushLimitThroughUnion;
import io.prestosql.sql.planner.iterative.rule.ReorderJoins;
import io.prestosql.sql.planner.iterative.rule.TransformUncorrelatedInPredicateSubqueryToJoin;
import io.prestosql.sql.planner.iterative.rule.TransformUncorrelatedInPredicateSubqueryToSemiJoin;
import io.prestosql.sql.planner.optimizations.LimitPushDown;
import io.prestosql.sql.planner.optimizations.PlanOptimizer;
import io.prestosql.sql.planner.plan.JoinNode;
import io.prestosql.sql.planner.plan.PlanNode;

import static io.prestosql.SystemSessionProperties.getJoinReorderingStrategy;

public class OptimizerUtils
{
    private OptimizerUtils()
    {
    }

    public static boolean isEnabledLegacy(PlanOptimizer optimizer, Session session)
    {
        if (optimizer instanceof SubQueryPushDown) {
            return SystemSessionProperties.isQueryPushDown(session);
        }
        if (optimizer instanceof LimitPushDown) {
            return SystemSessionProperties.isLimitPushDown(session);
        }
        if (optimizer instanceof HintedReorderJoins) {
            // Use the community ReorderJoins
            if (getJoinReorderingStrategy(session) != FeaturesConfig.JoinReorderingStrategy.NONE) {
                return false;
            }
            String joinOrder = SystemSessionProperties.getJoinOrder(session);
            return joinOrder != null && !"".equals(joinOrder);
        }
        return true;
    }

    public static boolean isEnabledRule(Rule<?> rule, Session session)
    {
        if (rule instanceof PushLimitThroughUnion) {
            return SystemSessionProperties.isPushLimitThroughUnion(session);
        }
        if (rule instanceof PushLimitThroughSemiJoin) {
            return SystemSessionProperties.isPushLimitThroughSemiJoin(session);
        }
        if (rule instanceof TransformUncorrelatedInPredicateSubqueryToJoin) {
            return SystemSessionProperties.isTransformUncorrelatedInToJoin(session);
        }
        if (rule instanceof TransformUncorrelatedInPredicateSubqueryToSemiJoin) {
            return !SystemSessionProperties.isTransformUncorrelatedInToJoin(session);
        }
        if (rule instanceof PushLimitThroughOuterJoin) {
            return SystemSessionProperties.isPushLimitThroughOuterJoin(session);
        }
        if (rule instanceof ReorderJoins) {
            // Use Hetu HintedReorderJoins
            String joinOrder = SystemSessionProperties.getJoinOrder(session);
            return joinOrder == null || "".equals(joinOrder);
        }
        return true;
    }

    public static boolean isEnabledLegacy(PlanOptimizer optimizer, Session session, PlanNode node)
    {
        if (optimizer instanceof IterativeOptimizer) {
            for (Rule<?> rule : ((IterativeOptimizer) optimizer).getRules()) {
                if (isEnabledRule(rule, session, node)) {
                    return true;
                }
            }
            // None of the rules are enabled
            return false;
        }
        return isEnabledLegacy(optimizer, session);
    }

    public static boolean isEnabledRule(Rule<?> rule, Session session, PlanNode node)
    {
        if (rule instanceof ReorderJoins) {
            // Use Hetu HintedReorderJoins
            String joinOrder = SystemSessionProperties.getJoinOrder(session);
            int threshold = SystemSessionProperties.getSkipReorderingThreshold(session);
            return threshold > 0 && (joinOrder == null || "".equals(joinOrder)) && !containsJoinNodesMoreThan(node, threshold);
        }
        return true;
    }

    private static boolean containsJoinNodesMoreThan(PlanNode node, int maxLimit)
    {
        JoinNodeCounter counter = new JoinNodeCounter(maxLimit);
        node.accept(counter, null);
        return counter.isMaxCountReached();
    }

    private static class JoinNodeCounter
            extends SimplePlanVisitor<Void>
    {
        private int count;
        private int maxLimit;

        JoinNodeCounter(int maxLimit)
        {
            this.maxLimit = maxLimit;
        }

        @Override
        public Void visitJoin(JoinNode node, Void context)
        {
            count++;
            if (count >= maxLimit) {
                // Break once reached the maximum count
                return null;
            }
            return super.visitJoin(node, context);
        }

        public boolean isMaxCountReached()
        {
            return count >= maxLimit;
        }
    }
}
